import uuid
import names
import urllib
import os

from flask import Flask
from flask import request, Response
from random import randint
from threading import Thread
from time import sleep

app = Flask(__name__)

server_address = "http://localhost:9023/api/smartwatch"

code_length = 1000

running = True

def change_heart_rate(heartRate, modify):
	heartRate += modify

	if heartRate < 80:
		modify = 1
	elif heartRate > 120:
		modify = -2
	elif heartRate > 110 and heartRate < 120:
		modify = randint(-1, 1)
	elif heartRate > 105:
		modify = 0.2

	print "Heartrate ", heartRate, " ", modify

	return heartRate, modify

def get_calories():
	heartRate = 80
	modify = 1

	while running:
		print "get calories"
		make_request(server_address + "/updateCalories", {"heartRate": heartRate})
		sleep(1)

		heartRate, modify = change_heart_rate(heartRate, modify)

t = Thread(target=get_calories)

def make_request(url, params):
	params["name"] = name
	params["port"] = port

	print params

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
def start():
	print "starting.."
	t.start()
	print "started.."
	return Response(status=200)

@app.route("/api/stop")
def stop():
	print "stopping.."
	on_exit()
	os._exit(1)
	print "stopped.."
	return Response(status=200)

def ping_server():
	make_request(server_address + "/ping", {})

def on_exit():
	print "Unpaired from smartphone"
	make_request(server_address + "/unpair", {})

import atexit
atexit.register(on_exit)

import signal

def handler(signum, frame):
	print "Ctrl z"
	on_exit()
	sys.exit(1)

if __name__ == "__main__":
	global name
	global pair_code
	global port	
	
	random = uuid.uuid4()
	pair_code = str(random)[0:6].upper()
	name = names.get_first_name()
	port = randint(9100, 9500)

	print "handler"
	signal.signal(signal.SIGTSTP, handler)

	print "Pairing code:", pair_code
	print "Smartwatch name:", name

	ping_server()

	app.run(port=port)
