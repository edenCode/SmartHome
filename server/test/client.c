#include <stdio.h>
#include <sys/types.h>	      
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/ip.h> /* superset of previous */
#include <stdlib.h>
#include <errno.h>
#include <strings.h>
#include <string.h>
#include <unistd.h>


//./a.out <ip> <port>
int main(int argc, const char *argv[])
{
    int connfd;
	char ch[] = "{\"userName\":\"lijie\",\"password\":\"12345\",\"phoneNumber\":\"12345678901\"}";
	unsigned char rch[1024] = {0};
	unsigned char recv[1024] = {0};
	rch[0] = 0xaa;
	rch[1] = 0x00;
	short *data_len = (short*)(&rch[2]);
	*data_len = strlen(ch) + 1;
	strncpy((rch+4),ch,*data_len);
	printf("rch[0]:%x\n",rch[0]);
	printf("rch[1]:%x\n",rch[1]);
	printf("rch[2,3]:%d\n",*data_len);
	printf("rchdata:%s\n",&rch[4]);

	

    if(argc != 3)
    {
		printf("Usage : %s <ip> <port>",argv[0]);
		exit(EXIT_FAILURE);
        //提示
    }

    //1.socket
    connfd = socket(AF_INET,SOCK_STREAM,0);
	if(connfd < 0)
	{
		perror("socket fail");
		exit(EXIT_FAILURE);
	}
    //2.bind
      //a.填充地址结构
      //b.绑定
	struct sockaddr_in saddr,saddr1;
	bzero(&saddr,sizeof(saddr));
	bzero(&saddr1,sizeof(saddr1));
//	struct in_addr sin_addr;
//	sin_addr.s_addr = inet_addr(argv[1]);

	saddr.sin_family = AF_INET;
	saddr.sin_port = htons(atoi(argv[2]));
	saddr.sin_addr.s_addr =  inet_addr(argv[1]);
	if(connect(connfd,(struct sockaddr*)&saddr,sizeof(saddr)) != 0 )
	{
		perror("bind fail");
		exit(EXIT_FAILURE);
	}

	

	int i = 0;
//	while(1)
	{
//		fgets(ch,sizeof(ch),stdin);
		write(connfd,rch,strlen(ch)+5);
		read(connfd,recv,sizeof(rch));
		printf("rcev:%s\n",recv+4);
//		printf("rc : %s",rch);
//		if(strncmp(rch,"quit",4) == 0)
//		{
//			close(connfd);
//			break;
//		}

	}


    //3.listen
//	if(listen(listenfd,5) != 0)
//	{
//		perror("listen fail");
//		exit(EXIT_FAILURE);
//	}
	

    //4.accept
//	socklen_t addrlen = sizeof(saddr1);
//    connfd = accept(listenfd,(struct sockaddr*)&saddr1,&addrlen);
//	if(connfd < 0)
//	{
//		perror("accept fail");
//		exit(EXIT_FAILURE);
//	}

    //打印对端的地址信息
    //测试：
    //telnet <ip> <port>
    //eg: 
    //   telnet 127.0.0.1 8888
//	printf("======================\n");
//	printf("IP: %d\n",inet_ntoa(saddr1.sin_addr));
//	printf("port: %d\n",ntohs(saddr1.sin_port));
//	printf("======================\n");
	printf("client exit.....\n");
	close(connfd);
    return 0;
}
