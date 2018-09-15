# -*- coding: UTF-8 -*-
#!/usr/bin/python
from http.server import BaseHTTPRequestHandler,HTTPServer
from urllib.parse import urlparse
from main import main
import numpy as np

#************自定义修改*************8
IP = '127.0.0.1'
PORT_NUMBER = 8898

#This class will handles any incoming request from
#the browser 
class myHandler(BaseHTTPRequestHandler):

    #Handler for the GET requests
    def do_GET(self):
        self.send_response(200)
        self.send_header('Content-type','text/html')
        self.end_headers()
        url = urlparse(self.path)
        print('protocol:', url.scheme)
        print ('hostname:', url.hostname)
        print ('port:', url.port)
        print ('path:', url.path)
        print ('query:', url.query)  # 查询参数，格式a=1
        if url.path == "/startGen":
            searchKey = str(url.query).split("&")[1]
            params = str(url.query).split("&")

            #**************自定义修改*************
            uri = "bolt://127.0.0.1:7687"
            graph_path = "/Users/mark/Documents/MasterLife/GraphDB/neo4j-community-3.4.6"

            main(params[0],params[1],params[2],params[3],params[4],params[5],self,uri,graph_path)
            self.wfile.write(("End!<br>").encode())
            return
        else:
            self.wfile.write(("BOM Failure！<br>").encode())
            return


try:
    #Create a web server and define the handler to manage the
    #incoming request
    server = HTTPServer((IP, PORT_NUMBER), myHandler)
    print('Started httpserver on port ' , PORT_NUMBER)

    #Wait forever for incoming htto requests
    server.serve_forever()

except KeyboardInterrupt:

    print('^C received, shutting down the web server')
    server.socket.close()