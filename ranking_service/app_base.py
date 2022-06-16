from flask import Flask

USER_NAME = 'root'
PASSWORD = 'smartcity'
IP_ADDRESS = '192.168.0.105'
PORT = '3306'
DATABASE_NAME = "smartcity"
SQLALCHEMY_DATABASE_URL = f"mysql+pymysql://{USER_NAME}:{PASSWORD}@{IP_ADDRESS}:{PORT}/{DATABASE_NAME}"

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = SQLALCHEMY_DATABASE_URL
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.jinja_env.auto_reload = True
app.config['TEMPLATES_AUTO_RELOAD'] = True


