import gym, time, socket, sys, struct
import time as time_
#import scipy.misc
from datetime import datetime

def millis():
	return int(round(time_.time() * 1000))

def readDouble(conn):
	message = ''
	packet = conn.recv(64)
	while packet.endswith('\n') != True:
		message += packet
		packet = conn.recv(64)
	else:
		message += packet
		message = message[:-1]

	return int(message)


env = gym.make('MountainCar-v0')
observation = env.reset()
img = env.render('rgb_array')
#scipy.misc.imsave('./../../../Javascript/liveStreaming/stream/image_stream.jpg', img)
itr = 2000

#Server part
HOST = ''
PORT = 9000

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print 'socket created'

try:
	s.bind((HOST, PORT))
except socket.error as msg:
	print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
	sys.exit(1)

print 'Socket bind complete'

s.listen(5)

conn, addr = s.accept()
message = ''
print 'Client connected'
for _ in range(1):
	for games in range(15):
		for spc in range(100):

			observation = env.reset()
		
			# print "test"
			# print str(observation.item(0))
			for _ in range(itr):
				# test1 = millis()
				# env.render()
				# test2 = test1 - millis()
				# print "here?"
				# print "Obs: " + str(observation) + " Reward: " + str(reward) + " "# + str(choice)
				if (observation.item(0) >= 0.5):
					print "win"
					conn.send((str(3) + '\n').encode())
					break
				else:
					# print "check"
					# test1 = millis()
					# conn.send((str(4) + "\n").encode())
					conn.send((str(4) + "\n") + (str(observation.item(0)) + "\n" + (str(observation.item(1)) + "\n").encode()))
					# conn.send((str(observation.item(1)) + "\n").encode())
					# print "doubleC"	

				# env.render()
				choice = readDouble(conn)
			
				# print str(choice)
				# print "choice is: " + str(choice)
				observation, reward, done, info = env.step(choice) # take a step

		
			# conn.send((str(5) + "\n").encode())
			curr = games*100 + spc
			print str(curr)



# Server part
# HOST = ''
# PORT = 9000

# s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# print 'socket created'

# try:
# 	s.bind((HOST, PORT))
# except socket.error as msg:
# 	print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
# 	sys.exit(1)

# print 'Socket bind complete'

# s.listen(5)

# conn, addr = s.accept()
# message = ''
# print 'Client connected'
# while 1:
	
# 	print str(message)

