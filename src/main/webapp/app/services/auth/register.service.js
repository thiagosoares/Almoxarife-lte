(function () {
    'use strict';

    angular
        .module('montisolTeste03App')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
