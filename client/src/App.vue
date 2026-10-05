<script setup>
import Main from './views/main.vue';
import Header from './components/basic/header.vue';
import Nav from './components/basic/nav.vue';
import Footer from './components/basic/footer.vue';
import ChatBot from "./components/chat/ChatBot.vue";

</script>

<template>
  <div id="app">
    <header><Header/></header>
    <nav><Nav/></nav>
    <main><Main/></main>
    <footer><Footer/></footer>

    <ChatBot/>
  </div>
</template>

<style>

</style>
