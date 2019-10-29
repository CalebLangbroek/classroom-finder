from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from bs4 import BeautifulSoup

# Initiate webdriver with url
driver = webdriver.Chrome()
driver.get('https://warden.ufv.ca:8910/prod/bwysched.p_select_term?wsea_code=CRED')

# Continues to next page with Winter 2020 Term
button = driver.find_element_by_xpath("//input[@type='submit']")
button.submit()

# Attempt to locate subject elements
subject_form = driver.find_element_by_name("sel_subj")


#subjects = driver.find_elements_by_xpath("/html/body/div[3]/form/font/table[2]/tbody/tr[1]/td[2]/select/option")[0]
#test = subjects.get_attribute()
#subject_text = subjects.text

#print (subject_text)
#[print(i) for i in subject_text]