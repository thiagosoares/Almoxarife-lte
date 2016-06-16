(function() {
    'use strict';

    angular
        .module('montisolTeste03App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('teste', {
            parent: 'entity',
            url: '/teste',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Testes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/teste/testes.html',
                    controller: 'TesteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('teste-detail', {
            parent: 'entity',
            url: '/teste/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Teste'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/teste/teste-detail.html',
                    controller: 'TesteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Teste', function($stateParams, Teste) {
                    return Teste.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('teste.new', {
            parent: 'teste',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/teste/teste-dialog.html',
                    controller: 'TesteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                descricao: null,
                                descricao2: null,
                                estoque: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('teste', null, { reload: true });
                }, function() {
                    $state.go('teste');
                });
            }]
        })
        .state('teste.edit', {
            parent: 'teste',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/teste/teste-dialog.html',
                    controller: 'TesteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Teste', function(Teste) {
                            return Teste.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('teste', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('teste.delete', {
            parent: 'teste',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/teste/teste-delete-dialog.html',
                    controller: 'TesteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Teste', function(Teste) {
                            return Teste.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('teste', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
