const menuItems = ['갈비탕', '비빔밥', '삼겹살', '파스타', '피자', '초밥','치킨','햄버거','마라탕','찜닭','텐동','짜장면','라멘','볶음밥'];
let currentIndex = 0;
const menuHighlight = document.getElementById('menuHighlight');

function typeWriter(text, index, callback) {
    if (index < text.length) {
        menuHighlight.innerHTML += text.charAt(index);
        setTimeout(() => typeWriter(text, index + 1, callback), 100);
    } else if (typeof callback == 'function') {
        setTimeout(callback, 1000);
    }
}

function changeMenu() {
    menuHighlight.innerHTML = '';
    let nextMenu = menuItems[currentIndex];
    typeWriter(nextMenu, 0, () => {
        currentIndex = (currentIndex + 1) % menuItems.length;
        setTimeout(changeMenu, 1000);
    });
}

changeMenu();