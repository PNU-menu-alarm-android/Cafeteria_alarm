from bs4 import BeautifulSoup
from selenium import webdriver
from collections import OrderedDict
import json
import re

def crawling():
    driver = webdriver.Chrome(r'C:\Users\HyewonKwak\Downloads\chromedriver_win32\chromedriver.exe')
    driver.get('http://www.pusan.ac.kr/kor/CMS/MenuMgr/menuListOnBuilding.do?mCode=MN202')
    driver.implicitly_wait(10)
    driver.find_element_by_xpath('//*[@id="multi-tab01"]/ul/li[2]/a').click()
    driver.implicitly_wait(10)
    filenameList = []
    for i in range(1, 7):#요일별 반복
        driver.find_element_by_xpath('//*[@id="childTab"]/div/ul/li['+str(i)+']/a').click()
        driver.implicitly_wait(20)
        soup = BeautifulSoup(driver.page_source, 'html.parser')
        menuDay = soup.select_one('#cont > div.menu-wr > div.menu-top.type-day > div > span.loca').get_text()
        menuDate = soup.select_one('#cont > div.menu-wr > div.menu-top.type-day > div > span.term').get_text()
        menuDate = menuDate.replace('.','_')
        filename = menuDay+'_'+menuDate+'.json'
        filenameList.append(filename)
        with open(filename,'w+',encoding='utf-8-sig',newline='') as file:
            cafeteria = OrderedDict()
            for j in range(1, 8):#식당별
                where = soup.select_one('#cont > div.menu-wr > div.is-wauto-box.menu-tbl-wr > table > tbody > tr:nth-child('+str(j)+') > th')
                where = where.get_text()
                where = where.replace('\n','')
                where = re.sub(r"[\n\t\s]*", "", where)
                newMenuDict = OrderedDict()
                for k in range(2,5):#조식 중식 석식
                    menuList = soup.select('#cont > div.menu-wr > div.is-wauto-box.menu-tbl-wr > table > tbody > tr:nth-child('+str(j)+') > td:nth-child('+str(k)+') > ul > li > p')
                    menuLists=[]
                    menuLists.extend(menuList)
                    newMenuList = []
                    for menu in menuList:
                        menu = menu.get_text()
                        menu = re.split(' |,|\n',menu)
                        newMenuList.extend(menu)

                    if len(newMenuList) == 0:#메뉴가 비어있는 칸
                        newMenuList.extend('x')

                    if k == 2:
                        newMenuDict['조식'] = newMenuList
                    elif k == 3:
                        newMenuDict['중식'] = newMenuList
                    else:
                        newMenuDict['석식'] = newMenuList
                cafeteria[where] = newMenuDict
            json.dump(cafeteria,file,ensure_ascii=False,indent='\t')
    return filenameList