from bs4 import BeautifulSoup
import requests, ssl
from requests import Session
from requests.packages.urllib3.poolmanager import PoolManager
from requests.adapters import HTTPAdapter


class Adapter(HTTPAdapter):
    """"Transport adapter" that allows us to use SSLv3."""

    def init_poolmanager(self, connections, maxsize, block=False):
        version = ssl.PROTOCOL_SSLv23
        self.poolmanager = PoolManager(
            num_pools=connections, maxsize=maxsize,
            block=block, ssl_version=version)

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) '
                  'Chrome/72.0.3626.121 Safari/537.36'}

with Session() as s:
    # Grab session key for post data
    s.mount("https://warden.ufv.ca:8910", Adapter())
    site = s.get("https://warden.ufv.ca:8910/prod/bwysched.p_select_term?wsea_code=CRED")
    bs_content = BeautifulSoup(site.content, "html.parser")
    token = bs_content.find("input", {"name": "sessionDataKey"})["value"]

    # Log in with any student info
    login_data = {"username": "USERNAME HERE", "password": "PASSWORD HERE", "sessionDataKey": token}
    s.post("https://identity.ufv.ca/commonauth", login_data)

    # The page we are wanting to scrape
    home_page = s.get("https://myportal.ufv.ca/")
    print(home_page.text)
