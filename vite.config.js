import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server:{
    host: 'localhost',
    port: 5173,
    proxy: {
      '/userApi': {
        target: 'http://localhost:10000/',
        changeOrigin: true,   //假装是自己请求的，没有跨站访问
        rewrite:(path) => path.replace(/^\/userApi/,'')
      },
      '/adminApi':{
        target:'http://localhost:9600/',
        changeOrigin: true,
        rewrite:(path) => path.replace(/^\/adminApi/,'')
      },
      '/orderApi':{
        target:'http://localhost:9800/',
        changeOrigin: true,
        rewrite:(path) => path.replace(/^\/orderApi/,'')
      },
      '/skuApi':{
        target:'http://localhost:9400/',
        changeOrigin: true,
        rewrite:(path) => path.replace(/^\/skuApi/,'')
      },
      '/skuSpecApi':{
        target:'http://localhost:9500/',
        changeOrigin: true,
        rewrite:(path) => path.replace(/^\/skuSpecApi/,'')
      },
      '/emailApi':{
        target:'http://localhost:9900/',
        changeOrigin: true,
        rewrite:(path) => path.replace(/^\/emailApi/,'')
      },
    }
  }
})
