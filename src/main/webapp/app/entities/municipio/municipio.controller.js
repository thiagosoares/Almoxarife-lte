(function() {
    'use strict';

    angular
        .module('montisolTeste03App')
        .controller('MunicipioController', MunicipioController);

    MunicipioController.$inject = ['$scope', '$state', 'Municipio'];

    function MunicipioController ($scope, $state, Municipio) {
        var vm = this;
        
        vm.municipios = [];

        loadAll();

        function loadAll() {
            Municipio.query(function(result) {
                vm.municipios = result;
            });
        }
    }
})();
