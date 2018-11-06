const mutations = {
  set_userinfo(state, info) {
    state.userInfo = info
  },
  set_order(state,order){
    state.order=order;
  },
  set_currentOrder(state,order){
    state.currentOrder=order;
  }
}

export default mutations
