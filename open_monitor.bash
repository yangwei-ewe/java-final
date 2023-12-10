#! /bin/bash
java -Djava.rmi.server.hostname=192.168.0.100 -Dcom.sun.management.jmxremote -Dcom.s
un.management.jmxremote.port=1995 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false src/http_server.java