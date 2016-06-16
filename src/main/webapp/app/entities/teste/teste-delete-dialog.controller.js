(function() {
    'use strict';

    angular
        .module('montisolTeste03App')
        .controller('TesteDeleteController',TesteDeleteController);

    TesteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Teste'];

    function TesteDeleteController($uibModalInstance, entity, Teste) {
        var vm = this;

        vm.teste = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Teste.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
