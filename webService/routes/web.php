<?php

Route::get('/', function () {
    return view('welcome');
});

Auth::routes();
Route::get('/home', 'HomeController@index')->name('home');
Route::resource('/cliente', 'ClienteController');
Route::resource('/producto', 'ProductoController');
Route::resource('/tienda', 'TiendaController');
Route::resource('/pedidos', 'TiendaController');
