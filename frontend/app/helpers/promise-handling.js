import Ember from 'ember';

export function promiseHandling(aPromise) {
  return aPromise.then(() => {
    aPromise
  }, (error) => {
    alert(error.statusText);
    error;
  });
}

