import uuid
import names
import urllib

from flask import Flask
from random import randint

app = Flask(__name__)

server_address = "http://localhost:9023/api"

@app.route("/api")
def hello():
    return "Hello World!"

def make_request(url, params):
	post_args = urllib.urlencode(params)

	response = urllib.urlopen(url + "?" + post_args).read()
	print "Response: ", response
	print

def ping_server(params):
	make_request(server_address + "/smartwatch/ping", params)

if __name__ == "__main__":
	random = uuid.uuid4()
	pair_code = str(random)[0:6].upper()
	global name
	name = names.get_first_name()
	port = randint(9100, 9500)

	print "Pairing code:", pair_code
	print "Smartwatch name:", name

	ping_server({"name": name})

	app.run(port=port)

def on_exit():
	print "Unpaired from smartphone"
	make_request(server_address + "/smartwatch/unpair", {"name": name})

import atexit
atexit.register(on_exit)