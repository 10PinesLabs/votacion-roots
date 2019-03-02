export function promiseHandling(aPromise) {
  return aPromise.then(() => {
    return aPromise;
  }, (error) => {
    alert(error.statusText);
    return error;
  });
}

