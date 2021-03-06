import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';

import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ChatService } from 'src/app/services/chat.service';
import { IChatMessage } from '../../interfaces/i-chat-message';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
})
export class ChatComponent implements OnInit {
  private stompClient: any;
  private CHANNEL = `/topic/chat`;
  private ENDPOINT = `http://206.189.35.196:8080/socket`;
  messages: IChatMessage[] = [];
  isConected = false;

  chatFormGroup: FormGroup = new FormGroup({
    message: new FormControl('', Validators.required),
  });
  constructor(private chatService: ChatService) {}

  ngOnInit(): void {
    this.connectWebsocket();
  }

  private connectWebsocket() {
    let ws = new SockJS(this.ENDPOINT);
    this.stompClient = Stomp.over(ws);
    // let that = this;
    this.stompClient.connect({}, () => {
      this.isConected = true;
      this.subscribeToGlobalChat();
    });
  }

  private subscribeToGlobalChat() {
    this.stompClient.subscribe(this.CHANNEL, (message: any) => {
      let newMessage = JSON.parse(message.body) as IChatMessage;
      console.log(newMessage);
      this.messages.push(newMessage);
    });
  }

  onSubmit() {
    let message = this.chatFormGroup.controls.message.value;
    if (!this.isConected) {
      alert(`please connect to WebSocket`);
      return;
    }
    this.chatService.postMessage(message).subscribe(
      (response) => {
        // console.log(response);
      },
      (error) => {
        console.log(error);
      }
    );
  }
}
