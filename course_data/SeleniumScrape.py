from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import Select
from selenium.webdriver.common.by import By
from bs4 import BeautifulSoup
import sqlite3

conn = sqlite3.connect('database.db')
# Initiate webdriver with url
driver = webdriver.Chrome()
driver.get('https://warden.ufv.ca:8910/prod/bwysched.p_select_term?wsea_code=CRED')


def main():
    # Select Fall 2019 in form
    sel_term = Select(driver.find_elements_by_name("term_code")[0])
    sel_term.select_by_value("201909")

    # Continues to next page with Fall 2019 Term
    button = driver.find_element_by_xpath("//input[@type='submit']")
    button.submit()

    # Used to get total number of subjects
    sel_subject = Select(driver.find_elements_by_name("sel_subj")[1])

    # Iterate through every index in subject
    for x in range(1, len(sel_subject.options)):

        # Select subject
        sel_subject = Select(driver.find_elements_by_name("sel_subj")[1])
        sel_subject.select_by_index(x)
        sel_subject.deselect_by_index('0')

        # Select Abbotsford Campus
        sel_campus = Select(driver.find_elements_by_name("sel_camp")[1])
        sel_campus.deselect_all()
        sel_campus.select_by_visible_text("Abbotsford")

        # Submit form
        button2 = driver.find_elements_by_xpath("//input[@type='submit']")[0]
        button2.submit()

        # If no courses in subject do not scrape
        if len(driver.find_elements_by_xpath("//span[@class='warningtext']")) != 0:
            print("No courses here!")
            return_to_subjects()
            continue

        # Calls function to scrape all course data
        scrape_page()

        # Once complete, return back to course selection
        return_to_subjects()


def delete_all():
    c = conn.cursor()
    c.execute('DELETE FROM courses')
    c.execute('DELETE FROM sections')
    c.execute('DELETE FROM times')
    c.execute('UPDATE sqlite_sequence SET seq = 0 WHERE name = ? OR name = ?', ('courses', 'times'))
    conn.commit()


def return_to_subjects():
    # Return back to subject selection
    return_search_button = driver.find_element_by_xpath(
        "//input[@type='submit' and @value='Return to Search']")
    return_search_button.click()


def scrape_page():
    c = conn.cursor()

    # SCRAPED DATA
    crn = ""

    # Iterates through total number of rows in table
    for x in range(2, len(driver.find_elements_by_xpath("/html/body/div[3]/form/font/table[2]/tbody/tr"))):

        # XPath locations
        base_loc = "/html/body/div[3]/form/font/table[2]/tbody/tr[" + str(
            x) + "]"
        subj_loc = base_loc + "/td[2]/a"
        crn_loc = base_loc + "/td[3]/a"
        title_loc = base_loc + "/td[4]/a"

        # Grabs data if subject location exists
        if len(driver.find_elements_by_xpath(subj_loc)) != 0:
            subject_section = driver.find_element_by_xpath(subj_loc).text
            crn = driver.find_element_by_xpath(crn_loc).text
            title = driver.find_element_by_xpath(title_loc).text
            section = subject_section[-3:]
            subject = subject_section[:-4]
            print(subject + ' ' + section)

            # Insert into courses into courses table if unique
            c.execute('INSERT OR IGNORE INTO courses(title, subject) VALUES (?, ?)', (subject, title))

            # Retrieves appropriate id from courses table to insert into sections table
            c.execute('SELECT id FROM courses WHERE title = ?', (subject,))
            course_id = c.fetchone()[0]

            # Insert row into sections if unique with appropriate course_id
            c.execute('INSERT OR IGNORE INTO sections(crn, course_id, title) VALUES (?, ?, ?)',
                      (crn, course_id, section))
            print(crn + " " + title)
            conn.commit()

        # XPath for schedule data
        date_loc = base_loc + "/td[2]/font"
        days_loc = base_loc + "/td[2]/font/font"
        time_loc = base_loc + "/td[2]/font/font/font"
        build_loc = base_loc + "/td[2]/font/font/font/font"
        room_loc = base_loc + "/td[2]/font/font/font/font/font"

        # Strings to remove
        date_text = "Dates: "
        time_text = "Time: "
        days_text = "Days: "
        build_text = "Building: "
        room_text = "Room: "

        # Selects rows that start with Dates:
        if len(driver.find_elements_by_xpath(date_loc)) != 0:
            if driver.find_element_by_xpath(date_loc + "/b").text == "Dates:":

                # Scrapes room text 
                # Adds to waste_text for later element scrapes
                # Removes "Room: " from beginning of string
                room = driver.find_element_by_xpath(room_loc).text
                waste_text = room
                room = room[len(room_text):]
                print(room)

                # Grabs full string subtracting excess text
                build = driver.find_element_by_xpath(
                    build_loc).text[:-(len(waste_text))]
                # Adds to excess text
                waste_text = build + waste_text
                # Add only building text to build
                build = build[len(build_text):]
                build_letter = build[-2:-1]
                print(build_letter)

                # Same as above but for time
                time = driver.find_element_by_xpath(
                    time_loc).text[:-(len(waste_text))]
                waste_text = time + waste_text
                time = time[len(time_text):]

                days = driver.find_element_by_xpath(
                    days_loc).text[:-(len(waste_text))]
                waste_text = days + waste_text
                days = days[len(days_text):]
                print(days)

                date = driver.find_element_by_xpath(
                    date_loc).text[:-(len(waste_text))]
                waste_text = date + waste_text
                date = date[len(date_text):]
                dates = date.split(" to ")
                sDate = dates[0]
                fDate = dates[1]
                print(sDate, fDate)

                if "Online" not in build:
                    room = build_letter + room
                    if len(time) != 0:
                        times = time.split(" - ")
                        sTime = times[0]
                        fTime = times[1]
                        print(sTime, fTime)
                        print(room)
                        # Locates id where room and building matches and inserts into table
                        c.execute('SELECT id FROM rooms WHERE id=?', (room,))
                        room_id = c.fetchone()

                        if room_id is not None:
                            print('room_id = ', room)
                            # Only insert into table if row is entirely unique
                            c.execute('SELECT * FROM times WHERE section_crn = ? AND room_id = ? AND day = ? AND '
                                      'start_time = ? AND end_time = ? AND start_date = ? AND end_date = ?',
                                      (crn, room, days, sTime, fTime, sDate, fDate))

                            existing_course = c.fetchone()
                            if existing_course is None:
                                c.execute(
                                    'INSERT INTO times(section_crn, room_id, day, start_time, end_time, start_date, '
                                    'end_date) VALUES (?, ?, ?, ? ,? ,? ,?)', (crn, room, days, sTime, fTime,
                                                                               sDate, fDate))
                                conn.commit()


if __name__ == "__main__":
    delete_all()
    main()
