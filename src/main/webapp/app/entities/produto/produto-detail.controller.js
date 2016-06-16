(function() {
    'use strict';

    angular
        .module('montisolTeste03App')
        .controller('ProdutoDetailController', ProdutoDetailController);

    ProdutoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Produto'];

    function ProdutoDetailController($scope, $rootScope, $stateParams, entity, Produto) {
        var vm = this;

        vm.produto = entity;

        var unsubscribe = $rootScope.$on('montisolTeste03App:produtoUpdate', function(event, result) {
            vm.produto = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
