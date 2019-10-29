from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import Select
from selenium.webdriver.common.by import By 
from bs4 import BeautifulSoup


def return_to_subjects():
    # Return back to subject selection
    return_search_button = driver.find_element_by_xpath("//input[@type='submit' and @value='Return to Search']")
    return_search_button.click()


# Initiate webdriver with url
driver = webdriver.Chrome()
driver.get('https://warden.ufv.ca:8910/prod/bwysched.p_select_term?wsea_code=CRED')

sel_term = Select(driver.find_elements_by_name("term_code")[0])
sel_term.select_by_value("201909")

# Continues to next page with Winter 2020 Term
button = driver.find_element_by_xpath("//input[@type='submit']")
button.submit()

# Used to get length of 
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

    ## SCRAPE COURSE DATA HERE

    # If no courses in subject do not scrape
    if (len(driver.find_elements_by_xpath("//span[@class='warningtext']")) != 0):
        print ("No courses here!")
        return_to_subjects()
        continue

    return_to_subjects()
