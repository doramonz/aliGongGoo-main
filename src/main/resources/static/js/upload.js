document.addEventListener('DOMContentLoaded', function () {
    document.querySelector('#gongGooSubmit').addEventListener('click', (e) => {
        e.preventDefault();
        const uri = document.querySelector('#uri');
        fetch('/api/products/upload?uri=' + encodeURIComponent(uri.value), {
            method: 'POST'
        }).then(response => {
            return response.json();
        }).then(json => {
            if(json['status'] === 200){
                alert('상품이 등록되었습니다.');
                location.href = '/';
            }else if(json['status'] === 400){
                alert(json['message']);
            }else {
                alert('서버 오류가 발생했습니다.');
            }
        })
    });
});