(function() {
    'use strict';

    angular
        .module('montisolTeste03App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('municipio', {
            parent: 'entity',
            url: '/municipio',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Municipios'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/municipio/municipios.html',
                    controller: 'MunicipioController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('municipio-detail', {
            parent: 'entity',
            url: '/municipio/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Municipio'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/municipio/municipio-detail.html',
                    controller: 'MunicipioDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Municipio', function($stateParams, Municipio) {
                    return Municipio.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('municipio.new', {
            parent: 'municipio',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/municipio/municipio-dialog.html',
                    controller: 'MunicipioDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                codigoIBGE: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('municipio', null, { reload: true });
                }, function() {
                    $state.go('municipio');
                });
            }]
        })
        .state('municipio.edit', {
            parent: 'municipio',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/municipio/municipio-dialog.html',
                    controller: 'MunicipioDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Municipio', function(Municipio) {
                            return Municipio.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('municipio', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('municipio.delete', {
            parent: 'municipio',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/municipio/municipio-delete-dialog.html',
                    controller: 'MunicipioDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Municipio', function(Municipio) {
                            return Municipio.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('municipio', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
