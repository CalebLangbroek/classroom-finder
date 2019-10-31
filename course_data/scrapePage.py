from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import Select
from selenium.webdriver.common.by import By
from bs4 import BeautifulSoup

# Initiate webdriver with url
driver = webdriver.Chrome()
driver.get('https://warden.ufv.ca:8910/prod/bwysched.p_select_term?wsea_code=CRED')

sel_term = Select(driver.find_elements_by_name("term_code")[0])
sel_term.select_by_value("201909")

# Continues to next page with Fall 2019 Term
button = driver.find_element_by_xpath("//input[@type='submit']")
button.submit()

# Used to get length of
sel_subject = Select(driver.find_elements_by_name("sel_subj")[1])

sel_subject.select_by_index('1')
sel_subject.deselect_by_index('0')

# Select Abbotsford Campus
sel_campus = Select(driver.find_elements_by_name("sel_camp")[1])
sel_campus.deselect_all()
sel_campus.select_by_visible_text("Abbotsford")

# Submit form
button2 = driver.find_elements_by_xpath("//input[@type='submit']")[0]
button2.submit()


# SCRAPED DATA
subject = ""
crn = ""
title = ""

date = list()
days = list()
time = list()
building = list()
room = list()

# Iterates through total nuber of rows in table
for x in range(len(driver.find_elements_by_xpath("/html/body/div[3]/form/font/table[2]/tbody/tr"))):

    # XPath locations
    base_loc = "/html/body/div[3]/form/font/table[2]/tbody/tr[" + str(
        x) + "]"
    subj_loc = base_loc + "/td[2]/a"
    crn_loc = base_loc + "/td[3]/a"
    title_loc = base_loc + "/td[4]/a"

    # Grabs data if subject location exists
    if (len(driver.find_elements_by_xpath(subj_loc)) != 0):
        subject = driver.find_element_by_xpath(subj_loc).text
        crn = driver.find_element_by_xpath(crn_loc).text
        title = driver.find_element_by_xpath(title_loc).text
        print(subject + " " + crn + " " + title)
    
    # TODO Fix xpaths, /font/text() doesnt work, neither does /font or /font/ or /font/text
    date_loc = base_loc + "/td[2]/font"
    days_loc = base_loc + "/td[2]/font/font"
    time_loc = base_loc + "/td[2]/font/font/font"
    building_loc = base_loc + "/td[2]/font/font/font/font"
    room_loc = base_loc + "/td[2]/font/font/font/font/font"

    # TODO WIP
    if (len(driver.find_elements_by_xpath(date_loc)) != 0):
        building = driver.find_element_by_xpath(building_loc).text
        
        print(building)