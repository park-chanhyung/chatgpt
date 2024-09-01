document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const cuisine = urlParams.get('cuisine');
    const requirements = urlParams.get('requirements');

    document.getElementById('selectedCategory').textContent = cuisine;
    document.getElementById('additionalRequirements').textContent = requirements || '없음';

    const loading = document.getElementById('loading');
    const error = document.getElementById('error');

    function getRecommendation(retryCount = 0) {
        loading.style.display = 'block';
        error.style.display = 'none';

        fetch('/getRecommendation', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ cuisine: cuisine, requirements: requirements })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('서버 응답 오류');
                }
                return response.json();
            })
            .then(data => {
                loading.style.display = 'none';
                if (data && data.recommendation) {
                    const parts = data.recommendation.split('추천 이유:');
                    const menu = parts[0].replace('추천 메뉴:', '').trim();
                    const reason = parts[1] ? parts[1].trim() : '추천 이유가 제공되지 않았습니다.';

                    typeWriter('recommendedMenu', menu);
                    setTimeout(() => typeWriter('recommendationReason', reason), menu.length * 100 + 500);
                } else {
                    throw new Error('추천 메뉴가 없습니다.');
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                loading.style.display = 'none';
                if (retryCount < 3) {
                    setTimeout(() => getRecommendation(retryCount + 1), 2000);
                } else {
                    error.textContent = '메뉴 추천을 가져오는 중 오류가 발생했습니다. 나중에 다시 시도해주세요.';
                    error.style.display = 'block';
                }
            });
    }

    function typeWriter(elementId, text, speed = 50) {
        let i = 0;
        const element = document.getElementById(elementId);
        element.innerHTML = '';

        function type() {
            if (i < text.length) {
                element.innerHTML += text.charAt(i);
                i++;
                setTimeout(type, speed);
            }
        }
        type();
    }

    getRecommendation();
});