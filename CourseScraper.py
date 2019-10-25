from bs4 import BeautifulSoup
import requests


soup = BeautifulSoup
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36'}

#Before entering term
url = 'https://warden.ufv.ca:8910/prod/bwskfcls.p_sel_crse_search'
# URL after entering term: 'https://warden.ufv.ca:8910/prod/bwckgens.p_proc_term_date'

#Form data input
post_params = {'p_term': '201909'}

page = requests.get(url, data = post_params, headers = headers)
soup = BeautifulSoup(page.content, 'html.parser')
