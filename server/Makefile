SERVER_PC:
	gcc UART/uart.c thread-pool/*.c  *.c -o $@ -lpthread -ljson -lsqlite3 -g
SERVER_ARM:
	arm-none-linux-gnueabi-gcc UART/uart.c thread-pool/*.c  *.c -o $@ -lpthread -L/home/ubuntu/work/server/jsonARM/lib -ljson -I/home/ubuntu/work/server/jsonARM/include/json-c -L./sqlite3ARM/lib -lsqlite3 -I./sqlite3ARM/include -g
	arm-none-linux-gnueabi-strip SERVER_ARM
clean:
	rm -rf *.o
