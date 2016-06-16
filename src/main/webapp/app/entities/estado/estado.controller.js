(function() {
    'use strict';

    angular
        .module('montisolTeste03App')
        .controller('EstadoController', EstadoController);

    EstadoController.$inject = ['$scope', '$state', 'Estado'];

    function EstadoController ($scope, $state, Estado) {
        var vm = this;
        
        vm.estados = [];

        loadAll();

        function loadAll() {
            Estado.query(function(result) {
                vm.estados = result;
            });
        }
    }
})();
