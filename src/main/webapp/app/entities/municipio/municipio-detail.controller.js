(function() {
    'use strict';

    angular
        .module('montisolTeste03App')
        .controller('MunicipioDetailController', MunicipioDetailController);

    MunicipioDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Municipio', 'Estado'];

    function MunicipioDetailController($scope, $rootScope, $stateParams, entity, Municipio, Estado) {
        var vm = this;

        vm.municipio = entity;

        var unsubscribe = $rootScope.$on('montisolTeste03App:municipioUpdate', function(event, result) {
            vm.municipio = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
