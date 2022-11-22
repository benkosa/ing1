const socket = io();

const messages = document.getElementById("messages");
const form = document.getElementById("form");
const input = document.getElementById("input");

const userName = prompt("Enter Your Name Please.");

socket.emit("user:join", userName);

socket.on("global:message", (message) => {
	messages.innerHTML += `
    <div class="join_message" >${message}
        <div class="message_time" >${getDateTime()}</div>
    </div>
    `;
});

socket.on("message:receive", (payload) => {
	messages.innerHTML += `          
    <div class="receive_message_container" >
        <span class="sent_message" >${payload.message}</span>
        <span class="receiver_name" > :${payload.name}</span>
        <div class="message_time" >${getDateTime()}</div>
    </div>
    `;
});

form.addEventListener("submit", (e) => {
	e.preventDefault();
	messages.innerHTML += `          
    <div class="sent_message_container" >
        <span class="your_name" >You: </span>
        <span class="sent_message" >${input.value}</span>
        <div class="message_time" >${getDateTime()}</div>
    </div>
    `;
	socket.emit("message:send", { name: userName, message: input.value });
	input.value = "";
});

function getDateTime() {
    return (new Date()).toLocaleString();
}
