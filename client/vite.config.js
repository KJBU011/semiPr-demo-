import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
// @ 경로 지정
import { fileURLToPath, URL } from 'node:url'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {      // <- @ 경로 지정
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },

})
