import uuid
import names
import urllib

from flask import Flask
from flask import request, Response
from random import randint

app = Flask(__name__)

server_address = "http://localhost:9023/api/smartwatch"

code_length = 1

def make_request(url, params):
	params["name"] = name
	params["port"] = port

	post_args = urllib.urlencode(params)

	response = urllib.urlopen(url + "?" + post_args).read()
	print "Response: ", response
	print



@app.route("/api/pair")
def pair():
	received_code = request.args.get('code')

	if received_code is not None and received_code == pair_code:
		return Response(status=200)

	return Response(status=400)


@app.route("/api/start")
def pair():
	weight = request.args.get('weight')
	age = request.args.get('age')

	make_request(server_address + "/start", {"weight": weight, "age": age})


@app.route("/api/stop")
def pair():
	make_request(server_address + "/stop")

def ping_server(params):
	make_request(server_address + "/ping", params)

if __name__ == "__main__":
	global name
	global pair_code
	global port

	random = uuid.uuid4()
	pair_code = str(random)[0:code_length].upper()
	name = names.get_first_name()[0:code_length]
	port = randint(9100, 9500)

	print "Pairing code:", pair_code
	print "Smartwatch name:", name

	ping_server()

	app.run(port=port)

def on_exit():
	print "Unpaired from smartphone"
	make_request(server_address + "/unpair", {})

import atexit
atexit.register(on_exit)