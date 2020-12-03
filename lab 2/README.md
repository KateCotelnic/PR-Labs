  #Short description

I've created a demo for a phone connection. Client makes what he wants until he takes the phone to enter the number (till a client enter "took"). 
Then there is building a secured "connection" between server and client. 
Client enters the number. If this number is not correct client receives error.
Server connects to subscriber. If subscriber don't answer or is busy, the client receive message.
Otherwise server make a connection between client and subscriber, and then they can communicate till they say "bye" to each  other.

#Description of the process

###Taking the phone
1. Client(client1) is listening for user's buffer reader and send each message to the client until the message is "took";
2. Server if listening the client until the message is "took";
3. Server creates public and private key for RSA encryption and decryption;
4. Client is listening to get public key from server for encryption;
5. Server sends public key (public exponent + public modulus) to server;

###Dial the phone number
1. Client is listening for user's input. It sends each message to server. User have 4 seconds to enter each number;
2. When the time's up client sends to server "call" message;
2. Server receives messages until it is "call" and then pul them together;
3. Client is waiting for server response;
4. If client sent two characters at one time, or the character was not a number, or the length of the got number is less than 2 or more than 15, or if number doesn't exist, server sends error;
5. In everything is ok, server connects to second client;

###Connection with the second client (client2)
1. Client2 waits for coming message;
2. Server sends to client2 message "Phone is calling";
3. Server creates public and private key and sends public key to client2;
3. Client2 listen for user's input. User can answer "answer" or "busy".User has 7 seconds to answer. The message is sent to server. If time's up message is "no answer";
4. Server receive message from client2;

###Making connection
1. If message got from client2 is "busy" or "no answer", server sends this message to client;
2. Otherwise, server sends to client1 port from client2;
3. Server disconnects;
4. Client1 connects with the port to client2 and send the message "connection";
5. Client2 receive the message "connection";

###Communication between clients
1. Clients create private and public keys and sends public keys for encryption of messages to each other;
2. Client2 listen for user's input, send message to client1, wait back message from client1;
3. Client1 receive the message from client2, then listen for user's input and send this message to client2;
4. Steps 2 and 3 repeats until the received messages will be "bye";

#Details description

##Sending messages
1. There is a number of max digits in one message: maxSize in transport layer;
2. Data from message is divided into parts and from each part is created a new message;
3. A new message contains index number of the part of all data, part of data and checksum;
4. While creating new messages they are added to the array messages;
5. The last message in the array with the information about the quantity of messages;
6. Last message is encrypted, sent and deleted;
7. Using a thread poll messages are encrypted and sent;

##Receiving messages
1. The fist message is received, decrypted. It contains quantity of coming messages;
2. Using a while loop messages are received and decrypted;
3. There is two lists of messages and their orders;
4. When received and decrypted, each message is devided in 3 parts: order, data and checksum;
5. First a new checksum is calculated for data and if there is an error: the message about error is send and new messages are received again. If this happens more then 5 times, the proccess is stoped;
6. Otherwise, each order is added to order list and each data is added to message list;
7. When all messages are received, using order list messages are stacked in the correct order in one message;
