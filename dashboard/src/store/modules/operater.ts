import { Store, Module, ActionContext } from "vuex";
import ListModule from "@/store/modules/base/list-module";
import IListState from "@/store/modules/base/list-state";
import Ajax from "@/lib/ajax";
import Operater from "@/store/entities/operater";
import PageResult from "@/store/entities/page-result";
import ListMutations from "@/store/modules/base/list-mutations";
interface IOperaterState extends IListState<Operater> {
  editOperater: Operater;
}
class CategoryMutations extends ListMutations<Operater> { }
class OperaterModule extends ListModule<IOperaterState, any, Operater> {
  state = {
    totalCount: 0,
    currentPage: 1,
    pageSize: 10,
    list: new Array<Operater>(),
    loading: false,
    editOperater: new Operater(),
  };
  actions = {
    async getAll(
      context: ActionContext<IOperaterState, any>,
      payload: any
    ): Promise<any> {
      context.state.loading = true;
      let reponse: any = await Ajax.post("/api/operater", payload.data);
      context.state.loading = false;
      let page: PageResult<Operater> = reponse.data as PageResult<Operater>;
      context.state.totalCount = page.total;
      context.state.list = page.records;
    },
    async all(
      context: ActionContext<IOperaterState, any>,
      payload: any
    ): Promise<any> {
      context.state.loading = true;
      let reponse: any = await Ajax.post("/api/operater/all");
      context.state.loading = false;
      let page: Array<any> = reponse.data as Array<Operater>;
      context.state.list = page;
    },
    async modify(
      context: ActionContext<IOperaterState, any>,
      payload: any
    ): Promise<any> {
      await Ajax.put("/api/operater", payload.data);
    },

    async delete(
      context: ActionContext<IOperaterState, any>,
      payload: any
    ): Promise<any> {
      await Ajax.delete("/api/operater/" + payload.data.id);
    },

    async batch(
      context: ActionContext<IOperaterState, any>,
      payload: any
    ): Promise<any> {
      await Ajax.post("/api/operater/batch", payload.data);
    },
    async get(
      context: ActionContext<IOperaterState, any>,
      payload: any
    ): Promise<any> {
      let reponse: any = await Ajax.get("/api/category/" + payload.data);
      context.state.editOperater = reponse.data as Operater;
    }
  };
  mutations = {
    setCurrentPage(state: IOperaterState, page: number): void {
      state.currentPage = page;
    },
    setPageSize(state: IOperaterState, pagesize: number): void {
      state.pageSize = pagesize;
    },
    edit(state: IOperaterState, de: Operater): void {
      state.editOperater = de;
    }
  };
}
const cateModule: OperaterModule = new OperaterModule();
export default cateModule;
