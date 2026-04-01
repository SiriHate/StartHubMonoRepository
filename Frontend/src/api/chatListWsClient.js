import { Client } from '@stomp/stompjs';
import config from '../config';
import { getCookie } from './apiClient';

const RECONNECT_DELAY_MS = 7000;
const HEARTBEAT_MS = 10000;

class ChatListWsClient {
    constructor() {
        this._client = null;
        this._onRefresh = null;
    }

    connect(onRefresh) {
        this.disconnect();

        this._onRefresh = onRefresh;
        const token = getCookie('accessToken');

        this._client = new Client({
            brokerURL: `${config.WS_URL}?token=${token}`,
            debug: () => {},
            reconnectDelay: RECONNECT_DELAY_MS,
            heartbeatIncoming: HEARTBEAT_MS,
            heartbeatOutgoing: HEARTBEAT_MS,

            onConnect: () => {
                this._client.subscribe('/user/queue/chat-list', () => {
                    try {
                        this._onRefresh?.();
                    } catch (_) {}
                });
            },

            onStompError: () => {},
            onWebSocketClose: () => {},
            onDisconnect: () => {},
        });

        this._client.activate();
    }

    disconnect() {
        if (this._client) {
            this._client.deactivate();
            this._client = null;
        }
        this._onRefresh = null;
    }
}

const chatListWsClient = new ChatListWsClient();
export default chatListWsClient;