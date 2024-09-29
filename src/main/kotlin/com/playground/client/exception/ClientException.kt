package com.playground.client.exception

import org.springframework.http.HttpStatus

class ClientException(message:String, status:HttpStatus): Exception(message)