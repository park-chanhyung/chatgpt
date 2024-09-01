document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const cuisine = urlParams.get('cuisine');
    document.getElementById('selectedCuisine').textContent = `선택한 메뉴: ${cuisine}`;

    const form = document.getElementById('menuDetailsForm');
    const loadingIndicator = document.getElementById('loadingIndicator');

    form.addEventListener('submit', function(e) {
        e.preventDefault();

        form.style.display = 'none';
        loadingIndicator.style.display = 'block';

        const requirements = document.getElementById('requirements').value;

        // 5초 후에 다음 페이지로 이동
        setTimeout(() => {
            window.location.href = `/recommendation?cuisine=${encodeURIComponent(cuisine)}&requirements=${encodeURIComponent(requirements)}`;
        }, 5000);
    });
});