(function() {
    'use strict';

    angular
        .module('montisolTeste03App')
        .controller('TesteDetailController', TesteDetailController);

    TesteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Teste'];

    function TesteDetailController($scope, $rootScope, $stateParams, entity, Teste) {
        var vm = this;

        vm.teste = entity;

        var unsubscribe = $rootScope.$on('montisolTeste03App:testeUpdate', function(event, result) {
            vm.teste = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
