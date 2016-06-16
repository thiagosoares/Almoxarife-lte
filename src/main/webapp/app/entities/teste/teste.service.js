(function() {
    'use strict';
    angular
        .module('montisolTeste03App')
        .factory('Teste', Teste);

    Teste.$inject = ['$resource'];

    function Teste ($resource) {
        var resourceUrl =  'api/testes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
