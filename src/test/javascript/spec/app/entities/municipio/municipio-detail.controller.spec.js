'use strict';

describe('Controller Tests', function() {

    describe('Municipio Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMunicipio, MockEstado;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMunicipio = jasmine.createSpy('MockMunicipio');
            MockEstado = jasmine.createSpy('MockEstado');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Municipio': MockMunicipio,
                'Estado': MockEstado
            };
            createController = function() {
                $injector.get('$controller')("MunicipioDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'montisolTeste03App:municipioUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
