package com.playground.client.exception

import org.springframework.http.HttpStatus

class ClientException(message:String, val responseStatus:HttpStatus): Exception(message)