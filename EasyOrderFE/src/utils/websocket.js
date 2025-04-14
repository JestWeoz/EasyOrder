import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

let stompClient = null
let reconnectAttempts = 0
const MAX_RECONNECT_ATTEMPTS = 5
const RECONNECT_DELAY = 5000 // 5 seconds

export const connectWebSocket = (onConnect, onError) => {
    const socket = new SockJS('http://localhost:8081/ws', null, {
        transports: ['websocket', 'xhr-streaming', 'xhr-polling'],
        withCredentials: true
    })

    stompClient = Stomp.over(socket)
    stompClient.debug = (str) => console.log(str)

    stompClient.connect({},
        () => {
            console.log('Connected to WebSocket')
            reconnectAttempts = 0
            if (onConnect) onConnect()
        },
        (error) => {
            console.error('STOMP error:', error)
            if (onError) onError(error)
            handleReconnect(onConnect, onError)
        }
    )
}

const handleReconnect = (onConnect, onError) => {
    if (reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
        reconnectAttempts++
        console.log(`Attempting to reconnect (${reconnectAttempts}/${MAX_RECONNECT_ATTEMPTS})...`)
        setTimeout(() => {
            connectWebSocket(onConnect, onError)
        }, RECONNECT_DELAY)
    } else {
        console.error('Max reconnection attempts reached')
    }
}

export const disconnectWebSocket = () => {
    if (stompClient && stompClient.connected) {
        stompClient.disconnect(() => {
            console.log('Disconnected from WebSocket')
        })
        stompClient = null
    }
}

export const sendMessage = (destination, message) => {
    if (stompClient && stompClient.connected) {
        stompClient.send(destination, {}, JSON.stringify(message))
        return true
    }
    return false
}

export const subscribe = (destination, callback) => {
    if (stompClient && stompClient.connected) {
        return stompClient.subscribe(destination, (message) => {
            callback(JSON.parse(message.body))
        })
    }
    return null
}

export const isConnected = () => {
    return stompClient && stompClient.connected
} 