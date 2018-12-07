<v-card>
    <v-toolbar flat>
        <v-toolbar-title>Visitor Statistic</v-toolbar-title>
    </v-toolbar>
    <v-layout pa-4 justify-end align-end>
        <v-layout column>
            <v-flex xs12 sm6 class="py-2">
                <v-btn-toggle v-model="staticBy">
                    <v-btn flat>
                        By Month
                    </v-btn>
                    <v-btn flat>
                        By District
                    </v-btn>
                    <v-btn flat>
                        By Kind
                    </v-btn>
                </v-btn-toggle>
            </v-flex>
            <v-flex v-if="staticBy == 0">
                <month-chart></month-chart>
            </v-flex>
            <v-flex v-if="staticBy == 1">
                <district-chart></district-chart>
            </v-flex>
            <v-flex v-if="staticBy == 2">
                <kind-chart></kind-chart>
            </v-flex>
        </v-layout>
    </v-layout>
</v-card>