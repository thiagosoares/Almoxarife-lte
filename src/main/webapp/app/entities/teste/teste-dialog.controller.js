(function() {
    'use strict';

    angular
        .module('montisolTeste03App')
        .controller('TesteDialogController', TesteDialogController);

    TesteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Teste'];

    function TesteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Teste) {
        var vm = this;

        vm.teste = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.teste.id !== null) {
                Teste.update(vm.teste, onSaveSuccess, onSaveError);
            } else {
                Teste.save(vm.teste, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('montisolTeste03App:testeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
