const fs = require('fs');
const path = require('path');

const configPath = path.join(__dirname, '../build/dist/js/productionExecutable/config.js');

// 환경변수 읽기
const apiBaseUrl = process.env.VITE_API_BASE_URL || 'http://localhost:8080';
const googleClientId = process.env.VITE_GOOGLE_CLIENT_ID || '';

// config.js 파일 읽기
let config = fs.readFileSync(configPath, 'utf8');

// 플레이스홀더 대체
config = config.replace('__API_BASE_URL__', apiBaseUrl);
config = config.replace('__GOOGLE_CLIENT_ID__', googleClientId);

// 파일 저장
fs.writeFileSync(configPath, config);

console.log('✅ Environment variables injected:');
console.log(`   API_BASE_URL: ${apiBaseUrl}`);
console.log(`   GOOGLE_CLIENT_ID: ${googleClientId ? '***' : '(empty)'}`);

