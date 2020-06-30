import json

from apscheduler.schedulers.background import BackgroundScheduler
from apscheduler.jobstores.base import JobLookupError


from menu_crawler import crawling
from firebase_admin import credentials
from firebase_admin import db
import firebase_admin
import time



def firebase():
    # for test
    db_url = 'https://pnumenualarm.firebaseio.com/'
    cred = credentials.Certificate(
        r"C:\Users\HyewonKwak\Downloads\pnumenualarm-firebase-adminsdk-f2r96-8cf8a9e399.json")
    default_app = firebase_admin.initialize_app(cred, {'databaseURL': db_url})

    fileNameList = crawling()
    print(fileNameList)
    day = ['월', '화', '수', '목', '금', '토']

    ref = db.reference()
    for i in range(0, 6):
        day_ref = ref.child(day[i])
        with open(fileNameList[i], 'r', encoding='utf-8-sig') as file:
            json_data = json.load(file)
            day_ref.set(json_data)
            print("나름 되고 있는 거 같다.")



class Scheduler:
    def __init__(self):
        self.sched = BackgroundScheduler()
        self.sched.start()
        self.job_id = ''

    def __del__(self):
        self.sched.shutdown()

    def shutdown(self):
        self.sched.shutdown()

    def stop(self, job_id):
        try:
            self.sched.remove_job(job_id)
        except JobLookupError as error:
            print(f"fail to stop Scheduler : {error}.")
            return

    def restart(self, job_id, run_time):    #run_time format : %H:%M
        hour, minute = map(int, run_time.split(':'))
        self.sched.reschedule_job(job_id, trigger='cron', hour=hour, minute=minute)

    def add_job(self, func_name, func_param, run_dayOfWeek, run_time, job_name): #run_time format : %H:%M
        hour, minute = map(int, run_time.split(':'))
        self.sched.add_job(func_name, args=func_param, trigger='cron', day_of_week=run_dayOfWeek, hour=hour, minute=minute, id=job_name, name=job_name)

if __name__ == '__main__':

    scheduler = Scheduler()
    scheduler.add_job(firebase,[], "mon-fri", "20:17", "hacksik crawling")
    while True:
        pass


