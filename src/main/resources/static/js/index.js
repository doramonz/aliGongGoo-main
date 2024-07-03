document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.getUri_button').forEach(button => {
        button.addEventListener('click', () => {
            const id = button.getAttribute('productId');

            fetch('/api/products/opened?productId=' + id)
                .then(response => {
                    return response.json();
                }).then(json => {
                if (json['status'] === 200) {
                    location.href = json['data'][0].url;
                } else if (json['status'] === 404) {
                    alert('열린 공구가 없습니다. 잠시 후 다시 시도하시거나, 공구를 등록해주세요.');
                } else {
                    alert('서버 오류가 발생했습니다.');
                }
            })
        });
    });
});