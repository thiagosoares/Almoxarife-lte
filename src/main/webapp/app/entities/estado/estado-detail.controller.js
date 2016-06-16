(function() {
    'use strict';

    angular
        .module('montisolTeste03App')
        .controller('EstadoDetailController', EstadoDetailController);

    EstadoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Estado'];

    function EstadoDetailController($scope, $rootScope, $stateParams, entity, Estado) {
        var vm = this;

        vm.estado = entity;

        var unsubscribe = $rootScope.$on('montisolTeste03App:estadoUpdate', function(event, result) {
            vm.estado = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
