superBetApp.controller('SureBetController', [ '$scope', 'resolvedDtos', 'Bets',
		'metamodel', function($scope, resolvedDtos, Bets, metamodel) {
			$scope.dtos = resolvedDtos;
			$scope.metamodel = metamodel;

			$scope.refresh = function() {
				$scope.dtos = service.sure();
			};
		} ]);