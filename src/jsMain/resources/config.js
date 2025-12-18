// 환경변수 설정 파일
// 배포 시 Vercel 환경변수로 대체됨

window.ENV = {
    API_BASE_URL: '__API_BASE_URL__',
    GOOGLE_CLIENT_ID: '__GOOGLE_CLIENT_ID__'
};

// 플레이스홀더가 대체되지 않은 경우 로컬 기본값 사용
if (window.ENV.API_BASE_URL === '__API_BASE_URL__') {
    window.ENV.API_BASE_URL = 'http://localhost:8080';
}
if (window.ENV.GOOGLE_CLIENT_ID === '__GOOGLE_CLIENT_ID__') {
    // ⚠️ 로컬 테스트 시 여기에 실제 Google Client ID 입력
    window.ENV.GOOGLE_CLIENT_ID = '772988401705-4io9tviphr75k075kb9lbrnmn960h2r8.apps.googleusercontent.com';
}
