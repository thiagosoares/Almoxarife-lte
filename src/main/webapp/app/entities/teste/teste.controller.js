(function() {
    'use strict';

    angular
        .module('montisolTeste03App')
        .controller('TesteController', TesteController);

    TesteController.$inject = ['$scope', '$state', 'Teste'];

    function TesteController ($scope, $state, Teste) {
        var vm = this;
        
        vm.testes = [];

        loadAll();

        function loadAll() {
            Teste.query(function(result) {
                vm.testes = result;
            });
        }
    }
})();
