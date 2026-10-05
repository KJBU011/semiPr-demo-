<template>
  <!-- 화면 우측, 헤더 바로 아래에 항상 떠 있는 챗봇 버튼 + 채팅창 -->
  <div class="chatbot-wrapper" ref="chatbotWrapper">
    <!-- 채팅창 토글 버튼 -->
    <button class="chatbot-toggle" @click="isOpen = !isOpen">
      {{ isOpen ? "닫기" : "💬 문의하기" }}
    </button>

    <!-- 채팅창 (열려있을 때만 노출) -->
    <div v-if="isOpen" class="chatbot-box">
      <div class="chatbot-header">
        주차장 안내 챗봇
        <!-- [추가] 우측 상단 닫기 버튼 -->
        <button class="chatbot-close" @click="isOpen = false">X</button>
      </div>

      <!-- 대화 내역 -->
      <div class="chatbot-messages" ref="messagesBox">
        <div
          v-for="(msg, idx) in messages"
          :key="idx"
          :class="['chatbot-msg-row', msg.role === 'user' ? 'row-user' : 'row-bot']"
        >
          <!-- [수정 2026-08-12] 아이콘을 메시지 왼쪽에 배치 (봇 메시지) -->
          <img
            v-if="msg.role !== 'user'"
            :src="chatbotIcon"
            class="bot-avatar"
            alt="챗봇 아이콘"
          />
          <div :class="['chatbot-msg', msg.role === 'user' ? 'msg-user' : 'msg-bot']">
            {{ msg.text }}
          </div>
          <!-- [추가 2026-08-12] 사용자 메시지 옆에 아이콘 표시 -->
          <img
            v-if="msg.role === 'user'"
            :src="userIcon"
            class="user-avatar"
            alt="사용자 아이콘"
          />
        </div>

        <!-- 답변 기다리는 동안 표시 -->
        <div v-if="isLoading" class="chatbot-msg-row row-bot">
          <img :src="chatbotIcon" class="bot-avatar" alt="챗봇 아이콘" />
          <div class="chatbot-msg msg-bot">답변 작성 중...</div>
        </div>
      </div>

      <!-- 입력창 -->
      <div class="chatbot-input-row">
        <input
          v-model="inputText"
          placeholder="궁금한 점을 입력해주세요"
          @keyup.enter="sendMessage"
          :disabled="isLoading"
        />
        <button @click="sendMessage" :disabled="isLoading || !inputText.trim()">
          전송
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
// [추가 2026-08-12] 챗봇 메시지 아이콘
// 실제 프로젝트 경로에 맞게 import 경로를 조절하세요 (예: src/assets/chatbot-icon.png)
import chatbotIconImg from "@/assets/chatbot-icon.png";
// [추가 2026-08-12] 사용자 메시지 아이콘
import userIconImg from "@/assets/user-icon.png";

export default {
  name: "ChatBot",

  data() {
    return {
      isOpen: false, // 채팅창 열림/닫힘 상태
      inputText: "", // 입력창 값
      isLoading: false, // 답변 기다리는 중인지 (중복 전송 방지 + 로딩 표시용)
      chatbotIcon: chatbotIconImg, // [추가 2026-08-12] 챗봇 메시지 아이콘
      userIcon: userIconImg, // [추가 2026-08-12] 사용자 메시지 아이콘
      // 대화 내역. role: 'user'(내가 보낸 것) / 'bot'(챗봇 답변)
      messages: [
        {
          role: "bot",
          text:
            "안녕하세요! 저는 병의원 상가 주차장 '메디컬타워 (MedicalTower)'의 안내 챗봇입니다. 주차 요금, 할인 정책 등이 궁금하시면 물어보세요.",
        },
      ],
    };
  },

  // [추가] 컴포넌트가 화면에 붙는 시점에, document 전체에 클릭 감지 리스너를 등록
  mounted() {
    document.addEventListener("click", this.handleOutsideClick);
  },

  // [추가] 컴포넌트가 없어질 때, 등록해둔 리스너도 같이 정리 (메모리 누수 방지)
  beforeUnmount() {
    document.removeEventListener("click", this.handleOutsideClick);
  },

  methods: {
    // [추가] 화면 어디를 클릭하든 이 함수가 호출됨.
    // 클릭된 지점(e.target)이 챗봇 영역(chatbotWrapper) 안쪽이 아니면 닫음.
    handleOutsideClick(e) {
      if (!this.isOpen) {
        return; // 이미 닫혀있으면 아무것도 안 함
      }
      const wrapper = this.$refs.chatbotWrapper;
      if (wrapper && !wrapper.contains(e.target)) {
        this.isOpen = false;
      }
    },
    sendMessage() {
      const text = this.inputText.trim();
      if (!text || this.isLoading) {
        return;
      }

      this.messages.push({ role: "user", text: text });
      this.inputText = "";
      this.isLoading = true;

      // [추가] sessionStorage에서 로그인 id를 꺼내서 message와 함께 전송
      const loginInfo = sessionStorage.getItem("login");
      const currentUserId = loginInfo ? JSON.parse(loginInfo).id : "";

      axios
        .post(
          "/chat",
          new URLSearchParams({ message: text, id: currentUserId })
        )
        .then((resp) => {
          this.messages.push({ role: "bot", text: resp.data });
        })
        .catch((err) => {
          this.messages.push({
            role: "bot",
            text: "오류가 발생했습니다. 다시 시도해주세요.",
          });
          console.error(err);
        })
        .finally(() => {
          this.isLoading = false;
          this.$nextTick(() => {
            const box = this.$refs.messagesBox;
            if (box) {
              box.scrollTop = box.scrollHeight;
            }
          });
        });
    },
  },
};
</script>

<!-- [수정 2026-08-12] 스타일을 별도 CSS 파일로 분리 -->
<style src="@/components/chat/css/ChatBot.css" scoped></style>
