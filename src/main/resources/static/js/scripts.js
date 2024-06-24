/*!
* Start Bootstrap - Shop Homepage v5.0.6 (https://startbootstrap.com/template/shop-homepage)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-shop-homepage/blob/master/LICENSE)
*/
// This file is intentionally blank
// Use this file to add JavaScript to your project

addEventListener('DOMContentLoaded', () => {
    function addProduct(id, image, name) {
        const productCard = document.createElement('div');
        productCard.className = 'col mb-5';
        productCard.innerHTML = `
            <div class="card h-100">
                <img class="card-img-top" src="${image}" alt="..." style="max-width: 100%; max-height: 100%; object-fit: contain;"/>
                <div class="card-body p-4">
                    <div class="text-center">
                        <h5 class="fw-bolder">${name}</h5>
                    </div>
                </div>
                <div class="card-footer p-4 pt-0 border-top-0 bg-transparent">
                    <div class="text-center"><a class="btn btn-outline-dark mt-auto" href="#">View options</a></div>
                </div>
            </div>
        `;
        document.getElementById('product-list').appendChild(productCard);
    }

    document.querySelector('#search-button').addEventListener('click', (e) => {
        e.preventDefault();
        const searchInput = document.querySelector('#search-input');
        const searchValue = searchInput.value;
        searchInput.value = '';
        window.location.search = `?name=${searchValue}`;

        const productList = document.getElementById('product-list');

        console.log(searchValue);
        fetch(`/api/products/search?name=${searchValue}`
            , {
                method: 'GET'
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(response.json()['message']);
            }).then(data => {
            console.log(data);

            document.getElementById('product-list').innerHTML = '';
            if (data['data'].length === 0) {
                productList.innerHTML = '<h1>상품이 없습니다.<br>추가 하기</h1>';
            } else {
                data['data'].forEach(product => {
                    addProduct(product.id, product.image, product.name);
                });
            }
        }).catch(error => {
            console.error(error);
        });
    });
});