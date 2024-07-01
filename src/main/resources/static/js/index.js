document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.getUri_button').forEach(button => {
        button.addEventListener('click', () => {
            const id = button.getAttribute('productId');

            fetch('/api/products/opened?productId=' + id)
                .then(response => response.json())
                .then(data => {
                    window.location.href = data['data'][0].url;
                });
        });
    });
});